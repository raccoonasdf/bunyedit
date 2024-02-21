0.3.0 [in progress]
-----
- Added support for specifying blocks by ID in commands

0.2.0
-----
- Restructured command code to make player and selection validity checks
  universal
- Changed coordinate format to `<x>,<y>,<z>` (comma-seperated) for easier
  parsing as a single argument
- Added `^<sway>,^<heave>,^<surge>` look-direction relative coordinates
- Added `//move` and `//movesel`
- Added `//stack`
- Added `//flip`

0.1.0
-----
- Initial release
- Added selections (`//cursor`, `//1`, `//2`, `//selection`)
- Added masks (`//mask`)
- Added set command (`//set`)
- Added undo history (`//undo`, `//redo`)
- Added copy buffer (`//copy`, `//paste`)
