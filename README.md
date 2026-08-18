# RabbitMorph

RabbitMorph is a Minecraft Forge 1.8.9 mod created by Jiwan that allows players to transform into a fully functional and customized rabbit using the vanilla Minecraft 1.8.9 rabbit model.

Official Repository: https://github.com/wlwlxks/RabbitMorph

## Features

- **Vanilla Rabbit Model**: Built directly on the standard 1.8.9 Minecraft rabbit geometry (head, ears, body, front legs, rear legs, tail).
- **Transformation System**: Server-authoritative morphing toggle with animated scaling, particle bursts (clouds and hearts), and vanilla rabbit sounds.
- **Customizable Types & RGB Color Channels**: Select from default presets (Normal, Brown, Black, White, Golden) and customize independent RGB values for Body, Ears, Eyes, and Tail.
- **360-Degree Live Preview**: Interactive Settings GUI featuring a large live preview supporting 360-degree drag rotation, pitch control, and mouse-wheel zoom.
- **First-Person Rabbit Paws**: Renders animated rabbit paws in first-person view while hiding human arms.
- **Configurable Attributes**: Independent settings for Health, Movement Speed, Jump Height, and Fall Damage multiplier.
- **Multiplayer Inventory Interaction**: Morph players can open non-rabbit human player inventories within 6 blocks using Shift + Interaction key, with strict server-side validation preventing rabbit-to-rabbit interaction.
- **Persistent Data**: Morph status and customization persist across respawns, dimension transfers, and logins.

## Requirements

- **Minecraft**: 1.8.9
- **Minecraft Forge**: 11.15.1.2318
- **Java**: Java 8 (OpenJDK 1.8.0 recommended)

## Building from Source

To build the mod JAR file on Windows:

```cmd
gradlew.bat clean build
```

The compiled output JAR will be generated at:
`build/libs/RabbitMorph-1.0.0.jar`

## Controls

Keybindings can be customized in Options -> Controls -> RabbitMorph:

- **R**: Rabbit Morph (Toggle transformation)
- **B**: Rabbit Settings (Open customization GUI)
- **E**: Rabbit Interaction (Interact with player inventory when sneaking)

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the LICENSE file for details.
