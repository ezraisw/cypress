# Cypress
Cytus inspired rhythm game made in Java.

## Requirements
- Java 8
- LibVLC

## Songs
For now, you can download an example song using the following link:
https://drive.google.com/file/d/1_v4mZWhhfv1Dm5ibRewdLmhKqnp0-8rO/view?usp=sharing

Extract the files to the directory where the jar files exist. The directory should look like this:
```
song/
cypress-{version}.jar
profile.json
```

## Controls
Song Select
- [`W`/`UP`] or [`S`/`DOWN`] select song
- [`A`/`LEFT`] or [`D`/`RIGHT`] select difficulty
- [`ENTER`] play song

Play
- [`Q`/`W`/`E`/`P`/`[`/`]`] hit notes on rails from left to right
- [`C`/`V`] trigger purple slide notes
- [`,`/`.`] trigger green slide notes

Result
- [`R`] Retry song
- [`ENTER`] Return to song select

## To-Do List
- Change scanline duration format to BPM
- Refactor code
- Finish long hold note
- Extra interface elements for guidance
