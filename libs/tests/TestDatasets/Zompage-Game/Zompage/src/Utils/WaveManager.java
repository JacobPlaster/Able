package Utils;

import Entities.Player;
import PowerPacks.PowerPack;
import Zombies.Zombie;
import Zombies.ZombieFast;
import Zombies.ZombieStandard;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class WaveManager 
{
	Array<Zombie> zombies;
	Player player;
	int zombieKills = 0;
	int totalZombiesSpawned = 0;
	int wave = 0;
	Array<PowerPack> powerPacks;
	
	// spawn locations of the zombies
	Vector3[] spawnLocations = 
		{
			new Vector3(200f, 0f, 200f),
			new Vector3(-200f, 0f, 200f),
			new Vector3(200f, 0f, -200f),
			new Vector3(-200f, 0f, -200f),
			new Vector3(200f, 0f, 50f),
			new Vector3(50f, 0f, 200f),
			new Vector3(150f, 0f, 200f),
			new Vector3(-220f, 0f, 10f),
			new Vector3(120f, 0f, -150f),
			new Vector3(40f, 0f, -200f),
			new Vector3(145f, 0f, -30f),
			new Vector3(230f, 0f, 120f),
			new Vector3(145f, 0f, -30f),
			new Vector3(135f, 0f, 300f)
		};
	
	// creates set waves
	// waves[0][0] = zombie standard (10 to spawn)
	// waves[0][1] = zombie fast (0 to spawn)a
	int[][] waves = new int[][]{
			  {10, 0},  // 1
			  {20, 0},  // 2
			  {15, 5},  // 3
			  {25, 3},  // 4
			  {25, 10}, // 5
			  {35, 2},  // 6
			  {35, 10}, // 7
			  {15, 20}, // 8
			  {20, 20}, // 9
			  {35, 20}, // 10
			  {35, 30}, // 11
			  {0, 50},  // 12
			  {0, 70}   // 13
			};
	
	// utilities
	MathsLibrary m = new MathsLibrary();
	
	
	/**
	 * Creates a manager which controls which sets which zombies spawn wach wave and when the waves spawn.
	 * It also creates set spawn locations for the zombies to spawn.
	 * This will be the only method for spawning zombies when deployed
	 * 
	 * @param Array containing all zombies
	 * @param The current existing player
	 * @paran Array containing all of the curre¡nt powerPacks
	 */
	public WaveManager(Array<Zombie> zombies, Player player, Array<PowerPack> powerPacks)
	{
		this.zombies = zombies;
		this.player = player;
		this.powerPacks = powerPacks;
	}
	
	public void render()
	{
		spawnWave(wave);
	}
	
	public void pause()
	{
		
	}
	
	public void resume()
	{
		
	}
	
	/**
	 * Spawns all of the zombies in the set wave. However, this method will only activate if all of the zombie in the past waves have been
	 * killed.
	 *
	 * @param int wave number
	 */
	public void spawnWave(int wave)
	{	
		
		// checks if all of the zombies in the past wave have died
		if(zombieKills == totalZombiesSpawned)
		{
			// uses spawn methods to quickly create zombies
			spawnNormal(waves[wave][0]);
			spawnFast(waves[wave][1]);
			// increments the wave to the next
			this.wave++;
		}
	}
	
	/**
	 * Spawns multiple normal zombies.
	 *
	 * @return int amount to spawn
	 */
	public void spawnNormal(int amount)
	{
		// for loop for creating multiple zombies
		for(int i = 0; i < amount; i++)
		{
			zombies.add(new ZombieStandard(spawnLocations[(int) m.randomNumber(0, spawnLocations.length)], player, powerPacks, zombies, this));
        	zombies.get(zombies.size - 1).create();
		}
		// adds to the total spawned zombies count
		totalZombiesSpawned += amount;
	}
	/**
	 * Spawns multiple fast zombies
	 *
	 * @return int amount to spawn
	 */
	public void spawnFast(int amount)
	{
		for(int i = 0; i < amount; i++)
		{
			zombies.add(new ZombieFast(spawnLocations[(int) m.randomNumber(0, spawnLocations.length)], player, powerPacks, zombies, this));
        	zombies.get(zombies.size - 1).create();
		}
		totalZombiesSpawned += amount;
	}
	
	/**
	 * Adds to the global kill count
	 */
	public void addKill()
	{
		this.zombieKills++;
	}
}
