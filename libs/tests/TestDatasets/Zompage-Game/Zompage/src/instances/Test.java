package instances;

import Entities.Entitie;
import Entities.Floor;
import Entities.Player;
import PowerPacks.PowerPack;
import UserInterface.AndroidJoystick;
import UserInterface.HealthHud;
import UserInterface.KeyboardController;
import UserInterface.TurretHud;
import Utils.MathsLibrary;
import Utils.WaveManager;
import Zombies.Zombie;

import com.WockaWocka.Zompage.ZompageGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Test extends Instance{
	
	int screen_width, screen_height;
	float aspect_ratio;
	Rectangle viewport;
	ZompageGame zompageGame;
	// Modal environment
	public ModelBuilder modalBuilder;
	public ModelBatch batch;
	// sprites
	public SpriteBatch sBatch;
	// Camera
	public PerspectiveCamera cam;
	float xPos = 0f, yPos = 80f, zPos = 20f;
	// Entities
	public Array<Entitie> entities = new Array<Entitie>();
	boolean inValidTurret = false;
	public Array<PowerPack> powerPacks = new Array<PowerPack>();
	// Zombies
	public Array<Zombie> zombies = new Array<Zombie>();
	WaveManager wManager;
	// Android analog sticks
	AndroidJoystick leftAnalog;
	AndroidJoystick rightAnalog;
	// Computer keyboard controller
	KeyboardController controller;
	// HUD
	TurretHud turretHud;
	HealthHud healthHud;
	// Player
	Player player;
	// utilities
	MathsLibrary m = new MathsLibrary();
	public String resolution;
	
	public Test(ZompageGame zompageGame, int screen_width, int screen_height, String res, float aspect_ratio)
	{
		this.screen_height = screen_height;
		this.screen_width = screen_width;
		this.aspect_ratio = aspect_ratio;
		this.zompageGame = zompageGame;
		this.resolution = res;
	}
	
	@Override
	public void create() {
		// creates a modal batch environment
		batch = new ModelBatch();
		sBatch = new SpriteBatch();
		// Creates a prespective camera (used for 3D)
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(xPos, yPos, zPos);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Entities
        // ALWAYS CALL entitie.get(yourEntitie).create() ALWAYS DO THIS!
        // entitie which need to be initialised before player
        healthHud = new HealthHud(resolution);
        
        entities.add(new Floor(200f));
        player = new Player((new Vector3(0f, 0f, 0f)), leftAnalog, rightAnalog, zombies, healthHud);
        player.create();
        for (int createdItems = 0; createdItems < entities.size; createdItems++)
        	entities.get(createdItems).create();
        for (int createdZombies = 0; createdZombies < zombies.size; createdZombies++)
        	zombies.get(createdZombies).create();
        for (int createdPowerPacks = 0; createdPowerPacks < powerPacks.size; createdPowerPacks++)
        	powerPacks.get(createdPowerPacks).create();
        
        // utilities
        wManager = new WaveManager(zombies, player, powerPacks);
        // HUD
        turretHud = new TurretHud(resolution, entities, player, zombies, healthHud);
        turretHud.create();
        healthHud.create();
        // for keyboard input
        controller = new KeyboardController(player);
        // Android input
        leftAnalog = new AndroidJoystick(resolution, "left", player);
        rightAnalog = new AndroidJoystick(resolution, "right", player);
        leftAnalog.create();
        rightAnalog.create();
	}
	
	@Override
	public void render() {
		// allows the cam to scale whith different devices
		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                (int) viewport.width, (int) viewport.height);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        Vector3 cPos = player.getPosition();
        cam.position.set(cPos.x, 60f, cPos.z + 30f);
        cam.lookAt(cPos.x, 0, cPos.z);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        batch.begin(cam);
        // renders all entities
        for (int renderedItems = 0; renderedItems < entities.size; renderedItems++)
        	entities.get(renderedItems).render(batch);
        for (int renderedZombies = 0; renderedZombies < zombies.size; renderedZombies++)
        	zombies.get(renderedZombies).render(batch);
        for (int renderedPowerPacks = 0; renderedPowerPacks < powerPacks.size; renderedPowerPacks++)
        	powerPacks.get(renderedPowerPacks).render(batch);
        
        player.render(batch);
        wManager.render();
        batch.end();
        // render sprite once 3d has finished
        sBatch.begin();
        leftAnalog.render(sBatch, rightAnalog);
        rightAnalog.render(sBatch, leftAnalog);
        turretHud.render(sBatch);
        healthHud.render(sBatch);
        sBatch.end();
        
        controller.render();
	}

	@Override
	public void pause() {
		for (int pausedItems = 0; pausedItems < entities.size; pausedItems++)
        	entities.get(pausedItems).pause();
		for (int pausedZombies = 0; pausedZombies < zombies.size; pausedZombies++)
        	zombies.get(pausedZombies).pause();
		for (int pausedPowerPacks = 0; pausedPowerPacks < powerPacks.size; pausedPowerPacks++)
        	powerPacks.get(pausedPowerPacks).pause();
		
		wManager.pause();
		leftAnalog.pause();
        rightAnalog.pause();
        player.pause();
        turretHud.pause();
        healthHud.pause();
	}

	@Override
	public void resume() {
		for (int resumedItems = 0; resumedItems < entities.size; resumedItems++)
        	entities.get(resumedItems).resume();
		for (int resumedZombies = 0; resumedZombies < zombies.size; resumedZombies++)
        	zombies.get(resumedZombies).resume();
		for (int resumedPowerPacks = 0; resumedPowerPacks < powerPacks.size; resumedPowerPacks++)
        	powerPacks.get(resumedPowerPacks).resume();
		
		wManager.resume();
		leftAnalog.resume();
        rightAnalog.resume();
        player.resume();
        turretHud.resume();
        healthHud.resume();
	}

	@Override
	public void resize(int width, int height) {
		// if the aspect ration of the current device is different to the enviroment
		// in which i developed it in, then scale it wither up or down to account for that
		// props goes to:   http://www.java-gaming.org/index.php?topic=25685.0
		float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f); 
        if(aspectRatio > aspect_ratio)
        {
            scale = (float)height/(float)screen_height;
            crop.x = (width - screen_width*scale)/2f;
        }
        else if(aspectRatio < aspect_ratio)
        {
            scale = (float)width/(float)screen_width;
            crop.y = (height - screen_height*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)screen_width;
        }
        float w = (float)screen_width*scale;
        float h = (float)screen_height*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
	}

	@Override
	public void dispose() {
		for (int disposedZombies = 0; disposedZombies < zombies.size; disposedZombies++)
        	zombies.get(disposedZombies).dispose();
		for (int disposedItems = 0; disposedItems < entities.size; disposedItems++)
        	entities.get(disposedItems).dispose();
		for (int disposedPowerPacks = 0; disposedPowerPacks < powerPacks.size; disposedPowerPacks++)
        	powerPacks.get(disposedPowerPacks).dispose();
		
		entities.clear();
		zombies.clear();
		powerPacks.clear();
		batch.dispose();
		sBatch.dispose();
		leftAnalog.dispose();
        rightAnalog.dispose();
        turretHud.dispose();
        player.dispose();
       healthHud.dispose();
	}
	
	public int getZombieKills()
	{
		int totalZombieKills = 0;
		for(int i = 0; i < zombies.size; i++)
		{
			if(zombies.get(i).isDead())
				totalZombieKills++;
		}
		return totalZombieKills;
	}
	
	@Override
	public String getName() {
		return "TestBed";
	}
}