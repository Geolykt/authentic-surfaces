package de.geolykt.surface.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.transformers.ASMTransformer;

public class CityGeneratorASM extends ASMTransformer implements TheotownClasses {

    private boolean valid = true;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals(CITY_GENERATOR)) {
            return false;
        }
        boolean transformed = false;
        for (MethodNode method : node.methods) {
            if (method.name.equals("generate") && method.desc.equals("(L" + SETTER + ";)L" + CITY + ";")) {
                for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                    if (insn.getOpcode() != Opcodes.INVOKEVIRTUAL) {
                        continue;
                    }
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;
                    if (methodInsn.owner.equals(CITY_CREATOR) && methodInsn.name.equals("create")) {
                        if (transformed) {
                            throw new IllegalStateException("Duplicate CityCreator#create call!");
                        }
                        InsnList injected = new InsnList();
                        int local = 0;
                        for (LocalVariableNode lvn : method.localVariables) {
                            if (lvn.desc.equals("L" + CITY_CREATOR + ";")) {
                                if (local != 0) {
                                    throw new IllegalStateException("Duplicate CityCreator instance within LVT!");
                                }
                                local = lvn.index;
                            }
                        }
                        if (local == 0) {
                            throw new IllegalStateException("Unable to find CityCreator instance within LVT!");
                        }
                        injected.add(new VarInsnNode(Opcodes.ALOAD, local));
                        injected.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        injected.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CITY_GENERATOR, "getAuthenticSurfaces", "()Z"));
                        injected.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CITY_CREATOR, "setAuthenticSurfaces", "(Z)V"));
                        method.instructions.insertBefore(insn, injected);
                        method.maxStack += 2;
                        transformed = true;
                    }
                }
            }
        }
        if (!transformed) {
            throw new IllegalStateException("Class that should be transformed not transformed: " + node.name);
        }
        valid = false;
        return transformed;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.equals(TheotownClasses.CITY_GENERATOR);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }
}
