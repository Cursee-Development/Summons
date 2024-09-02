package com.cursee.summons;

import com.cursee.summons.core.common.registry.RegistryNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SummonsNeoForge {

    public SummonsNeoForge(IEventBus modEventBus, ModContainer container) {

        Summons.init();

        RegistryNeoForge.register(modEventBus);
    }
}
