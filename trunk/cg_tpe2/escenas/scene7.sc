
image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 5.0 0.0 20.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

shader {
   name phong1
   type phong
   texture "earth.jpg"
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object {
   shader phong1
   type sphere
   name sphere2
   c 0 0 0
   r 5
}

