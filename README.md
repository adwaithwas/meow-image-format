# 🐾 Meow Image Format & Gallery

Welcome to the **Meow Image Format & Gallery** project! This repository contains the complete specification and implementation for the custom `.meow` binary image format, along with a sleek, feature-rich Java Swing viewer/converter interface.

---

## 🎨 The `.meow` Binary Specification

The `.meow` format is a lightweight, raw uncompressed binary image format designed to be simple, fast to parse, and extremely cute.

### File Header & Layout

| Offset (Bytes) | Data Type | Value / Description |
| :--- | :--- | :--- |
| `0x00 - 0x03` | ASCII String | `MEOW` (Magic Bytes identifier) |
| `0x04 - 0x07` | Big-Endian 32-bit Integer | Image **Height** (in pixels) |
| `0x08 - 0x0B` | Big-Endian 32-bit Integer | Image **Width** (in pixels) |
| `0x0C` onwards | Sequential Bytes | **RGB Pixel Data** in row-major order (3 bytes per pixel: Red, Green, Blue) |

---

## 🚀 Key Features

* **Custom Encoding/Decoding:** Seamless serialization of standard pixel matrices into the `.meow` binary format.
* **Universal Import/Convert:** Directly open `.jpg`, `.jpeg`, and `.png` files. They are automatically converted into `.meow` sequences in memory for immediate display.
* **Sleek Swing GUI Wrapper (`MeowGallery`):**
  * Modern, dark-themed image viewing canvas.
  * Drag-to-pan navigation to glide around high-resolution images.
  * Smooth mouse-wheel zooming (scroll up/down).
  * Menu-based file importing and exporting.
* **Standalone Executable:** Fully compiled into a single executable `MeowGallery.jar` file with zero external dependencies.

---

## 🖥️ Project Structure

* `MeowEncoder.java` — Core utility containing the static encoder implementation.
* `MeowViewer.java` — A lightweight, quick file-path viewer.
* `MeowGallery.java` — The main GUI application featuring menus, imports/exports, zooming, and panning.
* `MeowImagePanel.java` — Custom render canvas managing graphics scaling, pan coordinate translations, and viewport bounds.
* `MeowGallery.jar` — Pre-built executable binary.
* `test_images/` — Sample source images and encoded `.meow` files.

---

## 🛠️ Compilation & Running

### Prerequisites
* Java Development Kit (JDK) 8 or higher.

### Running the Executable JAR
You can run the application directly:
```bash
java -jar MeowGallery.jar
```

### Compiling from Source
1. Compile the source files:
   ```bash
   javac *.java
   ```
2. Run the main gallery app:
   ```bash
   java MeowGallery
   ```

---

## 📸 How to Use the Gallery App

1. **Open an Image:** Click `File` ➔ `Open Image`. You can choose `.meow`, `.jpg`, `.jpeg`, or `.png` files.
2. **Navigate:** 
   * **Zoom:** Use your mouse scroll wheel to zoom in and out.
   * **Pan:** Click and hold your left mouse button to drag the image around when zoomed in.
3. **Export as `.meow`:** Click `File` ➔ `Export as .meow` to save the currently viewed image in the custom `.meow` format at any location you choose.

---
*Developed with 🐾 and precision.*
