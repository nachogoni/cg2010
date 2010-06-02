
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 0.0 25.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 28
   p 0.0 0.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 28
   p -14.0 0.0 12.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 28
   p 14.0 0.0 12.5
}

shader {
   name stone0
   type stone
   depth 20
   diffuse_initial { "sRGB nonlinear" 0.30 0.30 0.0 }
   diffuse_final { "sRGB nonlinear" 01.0 01.0 01.0 }
}

object {
   shader stone0
   type sphere
   name stone
   c 0 0 0
   r 5
}

