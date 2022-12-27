package de.geolykt.surface.asm;

import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.transformers.ASMTransformer;
import de.geolykt.surface.draft.VirtualGroundDraft;

import info.flowersoft.theotown.map.objects.Ground;

public class CityCreatorASM extends ASMTransformer implements TheotownClasses {

    private boolean valid = true;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.startsWith(CITY_CREATOR) || !node.superName.equals("java/lang/Thread")) {
            return false;
        }
        for (MethodNode method : node.methods) {
            if (!method.name.equals("run") || !method.desc.equals("()V")) {
                continue;
            }
            AbstractInsnNode setFrameInsn = null;
            for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn.getOpcode() != Opcodes.INVOKEVIRTUAL) {
                    continue;
                }
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.owner.equals(GROUND) && methodInsn.name.equals("setFrame") && methodInsn.desc.equals("(I)V")) {
                    if (setFrameInsn != null) {
                        throw new IllegalStateException("Duplicate injection point detected within class " + node.name);
                    }
                    setFrameInsn = methodInsn;
                }
            }
            if (setFrameInsn == null) {
                continue; // Not our target, probably
            }
            AbstractInsnNode getDraftInsn = null;
            for (AbstractInsnNode insn = setFrameInsn.getPrevious(); insn != null; insn = insn.getPrevious()) {
                if (insn.getOpcode() != Opcodes.INVOKEVIRTUAL) {
                    continue;
                }
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.owner.equals(GROUND) && methodInsn.name.equals("getDraft") && methodInsn.desc.equals("()L" + GROUND_DRAFT + ";")) {
                    getDraftInsn = methodInsn;
                    break;
                }
            }
            if (getDraftInsn == null) {
                throw new IllegalStateException("Missing Ground#getDraft call within " + node.name + ".run:()V");
            }
            AbstractInsnNode previousInsn = getDraftInsn.getPrevious();
            while (previousInsn.getOpcode() == -1) {
                previousInsn = previousInsn.getPrevious();
            }
            if (previousInsn.getOpcode() != Opcodes.ALOAD) {
                throw new IllegalStateException("Unexpected opcode: " + previousInsn.getOpcode());
            }
            VarInsnNode loadGround = (VarInsnNode) previousInsn;
            LabelNode injectedLabel = new LabelNode();
            InsnList targetInject = new InsnList();
            targetInject.add(injectedLabel);
            method.instructions.insert(setFrameInsn, targetInject);
            InsnList injectedInstructions = new InsnList();
            injectedInstructions.add(new VarInsnNode(Opcodes.ALOAD, loadGround.var));
            injectedInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GROUND, "getDraft", "()L" + GROUND_DRAFT + ";"));
            injectedInstructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/geolykt/surface/draft/VirtualGroundDraft"));
            injectedInstructions.add(new JumpInsnNode(Opcodes.IFNE, injectedLabel)); // IFNE = Not equal 0
            while (!(previousInsn instanceof LineNumberNode)) {
                previousInsn = previousInsn.getPrevious();
            }
            method.instructions.insertBefore(previousInsn, injectedInstructions);
            valid = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.startsWith(CITY_CREATOR);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    public static final void helper(Ground ground, Random rnd) {
        if (!(ground.getDraft() instanceof VirtualGroundDraft)) {
            ground.setFrame(rnd.nextInt(ground.getDraft().frames.length));
        }
    }
}
