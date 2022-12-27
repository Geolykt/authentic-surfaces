package de.geolykt.surface;

import net.minestom.server.extras.selfmodification.MinestomRootClassLoader;

import de.geolykt.starloader.mod.Extension;
import de.geolykt.surface.asm.CityCreatorASM;
import de.geolykt.surface.asm.CityGeneratorASM;

public class AuthenticSurface extends Extension {

    static {
        MinestomRootClassLoader.getInstance().addTransformer(new CityGeneratorASM());
        MinestomRootClassLoader.getInstance().addTransformer(new CityCreatorASM());
    }
}
