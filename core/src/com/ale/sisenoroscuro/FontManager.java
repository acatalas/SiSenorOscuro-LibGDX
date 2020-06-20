package com.ale.sisenoroscuro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class FontManager {
    public static final float MESSAGE_LABEL_FONT_SIZE = 50;
    public static final float BUTTON_FONT_SIZE = 30;
    public static final float PLAYER_LABEL_FONT_SIZE = 22;
    private static final String BLACKCASTLE_FONT = "blackcastlemf";
    public static final String GREAT_VIBES_FONT = "greatvibes";
    public static final String GREAT_VIBES_FONT_FILE_NAME = GREAT_VIBES_FONT + ".ttf";
    public static final String BLACKCASTLE_FONT_FILE_NAME = BLACKCASTLE_FONT + ".ttf";
    private final AssetManager assetManager;

    public FontManager(AssetManager assetManager){
        FreeTypeFontGenerator.setMaxTextureSize(2048);
        this.assetManager = assetManager;
        FileHandleResolver resolver = new InternalFileHandleResolver();
        this.assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        this.assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    private void generateGreatVibesFont(float textSize){
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParameters = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParameters.fontFileName = GREAT_VIBES_FONT_FILE_NAME;

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = (int) (textSize * Gdx.graphics.getDensity());
        fontParameters.gamma = 0.8f;
        loaderParameters.fontParameters = fontParameters;
        //BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        //generator.dispose(); // don't forget to dispose to avoid memory leaks!
        assetManager.load(GREAT_VIBES_FONT + textSize + ".ttf", BitmapFont.class, loaderParameters);
        assetManager.finishLoading();
    }

    private void generateBlackCastleFont(float textSize, boolean shadow){
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParameters = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParameters.fontFileName = BLACKCASTLE_FONT_FILE_NAME;

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = (int) (textSize * Gdx.graphics.getDensity());
        fontParameters.gamma = 2f;
        fontParameters.renderCount = 2;
        fontParameters.minFilter = Texture.TextureFilter.Nearest;
        fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;

        if(shadow){
            fontParameters.shadowColor = Color.BLACK;
            fontParameters.shadowOffsetY = 2;
        }

        loaderParameters.fontParameters = fontParameters;

        assetManager.load(BLACKCASTLE_FONT + textSize + ".ttf" , BitmapFont.class, loaderParameters);
        assetManager.finishLoading();
    }

    public BitmapFont getGreatVibesFont(float textSize){
        if(!assetManager.contains(GREAT_VIBES_FONT)){
            generateGreatVibesFont(textSize);
        }
        return assetManager.get(GREAT_VIBES_FONT + textSize + ".ttf", BitmapFont.class);
    }

    public BitmapFont getBlackCastleFont(float textSize, boolean shadow){
        if(!assetManager.contains(BLACKCASTLE_FONT)){
            generateBlackCastleFont(textSize, shadow);
        }
        return assetManager.get(BLACKCASTLE_FONT + textSize + ".ttf", BitmapFont.class);
    }
}
