
# Physics Bound loop with capped FPS

## V1

0. Process input
1. Tick until required TPS
2. Render + afterprocess until
   - end of second
     - immediately start the next second
   - required FPS
     - sleep until the end of the second

Flawed because all of the ticks run at the same time in the beginning of the second. This makes user input useless for the rest of the frame. The game also doesn't render untill it's done with all of the updates, so aside the first frame, the rest don't show anything new.

## V2

Calculate
