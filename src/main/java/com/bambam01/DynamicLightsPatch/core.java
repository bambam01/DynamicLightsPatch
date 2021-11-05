package com.bambam01.DynamicLightsPatch;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"com.bambam01.DynamicLightsPatch"})
@IFMLLoadingPlugin.SortingIndex(10001)
@IFMLLoadingPlugin.Name("DynamicLightsPatch")

public class core implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.bambam01.DynamicLightsPatch.DynamicLightsPatchTransformer"};
    }

    @Override
    public String getModContainerClass() {

        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
