
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 0.0 5.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 8
   p 0.0 0.0 5.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 8
   p -4.0 0.0 2.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 8
   p 4.0 0.0 2.5
}

shader {
   name stone0
   type stone
   diffuse_initial { "sRGB nonlinear" 1.0 1.0 1.0 }
   diffuse_final { "sRGB nonlinear" 0.0 0.0 0.0 }
}

object {
   shader stone0
   type plane
   name stone
   p 0 0 0
   n 0 0 1
}

