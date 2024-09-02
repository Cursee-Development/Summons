package com.cursee.summons;

import net.fabricmc.api.ModInitializer;

public class SummonsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        Summons.init();
    }
}
