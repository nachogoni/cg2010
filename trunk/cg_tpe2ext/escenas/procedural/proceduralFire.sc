
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 0.0 40.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p 0.0 15.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p 0.0 -15.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p 0.0 0.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p -14.0 0.0 12.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p 14.0 0.0 12.5
}

shader {
   name fire0
   type fire
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.0 0.8 0.0 }
   diffuse_final { "sRGB nonlinear" 1.0 0.0 0.0 }
   }

object {
   shader fire0
   type sphere
   name fire
   c 0 0 0
   r 10
}

