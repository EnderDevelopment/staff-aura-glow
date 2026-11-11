# Staff Aura Glow Minecraft Plugin

Staff Aura Glow is a lightweight Minecraft plugin that adds a subtle magical aura effect to a custom magic staff while it is held in the player's main hand. This Minecraft plugin uses Paper particles, a unique staff identifier, and a simple command to provide a quiet visual effect without adding gameplay powers or excessive particle effects.

## Install

1. Use Java 21 or newer.
2. Run a Paper Minecraft server compatible with Minecraft 1.21.11.
3. Build the plugin with Maven:
   ```bash
   mvn package
   ```
4. Place the generated `target/plugin.jar` file in the server's `plugins` folder.
5. Start or restart the Paper server.
6. Confirm that the plugin named `MagicStaff` loads successfully.

## Use

1. Join the server as a player.
2. Run:
   ```text
   /magicstaff
   ```
3. The plugin gives you a Weathered Oak Staff.
4. Place the staff in your main hand.
5. Hold the staff to display a subtle aura made from End Rod and enchantment particles around the player.
6. Switch items, leave the server, or stop holding the staff to stop the aura.

The staff is identified using persistent item data, so only staffs created by this plugin activate the effect. If your inventory is full, the staff is dropped naturally at your location.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/1465d80b-a34a-4902-8232-f94bdf2a3978?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=1465d80b-a34a-4902-8232-f94bdf2a3978
