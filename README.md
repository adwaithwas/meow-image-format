# meow image format & gallery

i created this image format just for fun, as i was learning about how images encoding and computer graphics actually work. so i decided to attempt to create a basic image render format myself to get a better deep understanding of this topic. this is not a perfect image format and is not intented to be used as a regular day to day thing, as it is poor in areas like compression, etc, which is fine because this is just a passion project. i created a jframe wrapper for my back end to get a betetr hands on experience for my project, and i may or may not improvise this with other features like better compression and color modes, but fr now this is it and im hapy with this project so far.

i learned a lot about computer graphics and byte encoding while making this project and readin documentation was a nice experience. i used little to no ai, excpet for debugging some parts of jframe but its ok ig lol.

## how the format works (.meow format spec)

its a very basic custom binary layout:
- Magic bytes: "MEOW" (4 bytes)
- Height: 32-bit integer (4 bytes)
- Width: 32-bit integer (4 bytes)
- Pixel data: Sequential RGB bytes (3 bytes per pixel) for every pixel starting from top-left.

## features

- **viewer & gallery:** a clean black-themed window to open and see your images.
- **zoom & pan:** you can use your mouse scroll wheel to zoom, and drag with left click to pan around.
- **open standard images:** directly opens `.png` and `.jpg` (it converts them in memory so they load up).
- **export to .meow:** converts standard image types and saves them as a `.meow` file.
- **no dependencies:** compiled as a single runnable `.jar` file.

## how to run

1. Make sure you have java installed.
2. Download the `MeowGallery.jar` file.
3. Run it using the terminal or just double click it:
   ```bash
   java -jar MeowGallery.jar
   ```
