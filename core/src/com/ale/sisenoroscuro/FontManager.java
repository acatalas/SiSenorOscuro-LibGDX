package com.ale.sisenoroscuro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Disposable;

import java.awt.Button;

public class FontManager {
    public static final String GREAT_VIBES_FONT = "greatvibes.ttf";
    public static final String BLACKCASTLE_FONT = "blackcastlemf.ttf";
    private AssetManager assetManager;

    public FontManager(AssetManager assetManager){
        this.assetManager = assetManager;
        FileHandleResolver resolver = new InternalFileHandleResolver();
        this.assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        this.assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    private void generateCreatVibesFont(int textSize){
        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(GREAT_VIBES_FONT));
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParameters = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParameters.fontFileName = GREAT_VIBES_FONT;
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = (int) (textSize * Gdx.graphics.getDensity());
        fontParameters.gamma = 0.8f;
        loaderParameters.fontParameters = fontParameters;
        //BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        //generator.dispose(); // don't forget to dispose to avoid memory leaks!
        assetManager.load(GREAT_VIBES_FONT, BitmapFont.class, loaderParameters);
        assetManager.finishLoading();
    }

    private void generateBlacCastleFont(int textSize){
        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(BLACKCASTLE_FONT));
        //generator.scaleForPixelHeight((int)Math.ceil(textSize * Gdx.graphics.getDensity()));
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParameters = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParameters.fontFileName = BLACKCASTLE_FONT;

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = (int)Math.ceil(textSize * Gdx.graphics.getDensity());
        fontParameters.gamma = 3f;
        fontParameters.mono = true;
        fontParameters.renderCount = 2;
        fontParameters.minFilter = Texture.TextureFilter.Nearest;
        fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;

        //BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        //generator.dispose(); // don't forget to dispose to avoid memory leaks!
        //return font;

        assetManager.load(BLACKCASTLE_FONT, BitmapFont.class, loaderParameters);
        assetManager.finishLoading();

    }

    public BitmapFont getGreatVibesFont(int textSize){
        if(!assetManager.contains(GREAT_VIBES_FONT)){
            generateCreatVibesFont(textSize);
        }
        return assetManager.get(GREAT_VIBES_FONT, BitmapFont.class);
    }

    public BitmapFont getBlackCastleFont(int textSize){
        if(!assetManager.contains(BLACKCASTLE_FONT)){
            generateBlacCastleFont(textSize);
        }
        return assetManager.get(BLACKCASTLE_FONT, BitmapFont.class);
    }
}
