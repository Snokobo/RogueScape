package com.RogueScape;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.image.BufferedImage;

import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.NpcID;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
	name = "RogueScape"
)

public class RogueScapePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	private RogueScapePanel panel;
	private NavigationButton navButton;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Gson gson;

	private RogueScapeState state;

	@Inject
	private RogueScapeConfig config;

	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	private boolean unlockTriggered = false;

	@Override
	protected void startUp() throws Exception
	{
		loadState();
		BufferedImage icon = ImageUtil.loadImageResource(RogueScapePlugin.class, "/com/RogueScape/RogueScapeIcon.png");
		panel = new RogueScapePanel(this);

		panel.refreshUnlockedList(state);

		navButton = NavigationButton.builder()
				.tooltip("RogueScape")
				.icon(icon)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
		log.debug("RogueScape started!");
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RogueScape Started!", null);

	}

	@Override
	protected void shutDown() throws Exception
	{
		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
		}
		log.debug("RogueScape stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	private static final Set<Integer> RAT_IDS = Set.of(
			NpcID.RAT_2854
	);

	public void applyUnlock(UnlockOption option){
		switch (option.getType()){
			case ITEM:
				state.unlockedItems.add(option.getId());
				break;
			case SKILL:
				state.unlockedSkills.add(option.getId());
				break;
			case QUEST:
				state.unlockedQuests.add(option.getId());
				break;
		}
	}

//	public void unlock(UnlockOption option)
//	{
//		state.unlockedItems.add(option.getId());
//		saveState();
//	}

//	private List<UnlockOption> getUnlockOptions()
//	{
//		List<UnlockOption> allUnlocks = List.of(
//				new UnlockOption("BRONZE_SWORD", "Bronze Sword","",UnlockType.ITEM),
//				new UnlockOption("IRON_SWORD", "Iron Sword","",UnlockType.ITEM),
//				new UnlockOption("ATTACK_SKILL", "Attack skill","",UnlockType.SKILL)
//		);
//
//		List<UnlockOption> locked = new ArrayList<>();
//		for (UnlockOption option : allUnlocks) {
//			if (!state.isUnlocked(option)) {
//				locked.add(option);
//			}
//		}
//
//		Collections.shuffle(locked);
//		return locked.size() > 5 ? locked.subList(0, 5) : locked;
//	}

	public void sendChatMessage(String message)
	{
		// Make sure the client is available
		if (client != null) {
			client.addChatMessage(
					ChatMessageType.GAMEMESSAGE,  // system message type
					"",                            // sender (empty for system)
					message,                       // actual message
					null                           // optional message ID, null is fine
			);
		}
	}

	private void giveUnlockChoice() {
		if (unlockTriggered) return;

		unlockTriggered = true;

		// Step 1: Collect all locked options from all types
		List<UnlockOption> allLocked = new ArrayList<>();
		allLocked.addAll(getLockedOptions(UnlockType.ITEM));
		allLocked.addAll(getLockedOptions(UnlockType.SKILL));
		allLocked.addAll(getLockedOptions(UnlockType.QUEST));

		// Step 2: Roll a few random choices adaptively
		List<UnlockOption> choices = rollUnlockChoices(allLocked, 5); // 5 options max

		// Step 3: Open the chatbox panel with these choices
		if (!choices.isEmpty()) {
			openUnlockChatbox(choices);
		} else {
			sendChatMessage("No unlocks available.");
		}
	}

	private List<UnlockOption> getLockedOptions(UnlockType type) {
		List<UnlockOption> locked = new ArrayList<>();

		switch (type) {
			case ITEM:
				for (ItemData item : ItemRepository.getAllItems()) {
					if (!state.unlockedItems.contains(item.getId())) {
						locked.add(new UnlockOption(
								item.getId(),       // id
								item.getName(),     // name
								item.getDescription(), // description
								UnlockType.ITEM     // type
						));
					}
				}
				break;

			case SKILL:
				// placeholder until skill logic is implemented
				// locked.addAll(getLockedSkills());
				break;

			case QUEST:
				// placeholder until quest logic is implemented
				// locked.addAll(getLockedQuests());
				break;
		}

		return locked;
	}

	private List<UnlockOption> rollUnlockChoices(List<UnlockOption> options, int maxChoices) {
		if (options.isEmpty()) {
			return new ArrayList<>();
		}

		Collections.shuffle(options);
		int numToPick = Math.min(maxChoices, options.size());
		return new ArrayList<>(options.subList(0, numToPick));
	}

	private void openUnlockChatbox(List<UnlockOption> choices)
	{
		if (choices.isEmpty()) {
			// Nothing to show
			return;
		}

		// Remove any previous content in the container
		panel.removeAll();

		// Always create a new panel instance
		RogueScapeChatboxPanel unlockPanel = new RogueScapeChatboxPanel(this, choices, chatboxPanelManager);

		// Add the new panel safely
		panel.add(unlockPanel);

		// Refresh the container
		panel.revalidate();
		panel.repaint();
	}

	public void resetUnlocks() {
		state.unlockedItems.clear();  // clear unlocked items
		// optionally clear unlocked skills/quests when implemented
		saveState();                  // save the cleared state

		// Refresh the panel to show empty list
		if (panel != null) {
			panel.refreshUnlockedList(state);
		}

		sendChatMessage("All unlocks have been reset!");
	}

	public void selectUnlock(UnlockOption option)
	{
		applyUnlock(option);
		saveState();

		sendChatMessage("Unlocked: " + option.getName());

		if (panel != null) {
			panel.refreshUnlockedList(state);
		}
	}

	private void loadState()
	{
		String json = configManager.getConfiguration("roguelike", "state");
		if (json == null)
		{
			state = new RogueScapeState();
			saveState();
		}
		{
			state = gson.fromJson(json, RogueScapeState.class);
		}
	}

	private boolean shouldTriggerUnlock(NPC npc)
	{
		// Example: only trigger for rats and goblins
		int id = npc.getId();
		return RAT_IDS.contains(id);
	}

	private void saveState()
	{
		String json = gson.toJson(state);
		configManager.setConfiguration("roguelike", "state", json);
	}

	@Subscribe
	public void onActorDeath(ActorDeath event)
	{
		Actor actor = event.getActor();

		if (!(actor instanceof NPC))
		{
			return;
		}

		NPC npc = (NPC) actor;
		if (!shouldTriggerUnlock(npc)) return;

		if (npc.getInteracting() != client.getLocalPlayer())
		{
			return;
		}

		if (!RAT_IDS.contains(npc.getId()))
		{
			return;
		}
		giveUnlockChoice();
	}

	@Provides
	RogueScapeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RogueScapeConfig.class);
	}
}
