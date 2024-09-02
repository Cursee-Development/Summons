package com.cursee.summons;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SummonsForge {
    
    public SummonsForge() {
    
        Summons.init();
    }
}
