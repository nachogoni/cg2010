
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 0.0 20.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 0.0 0.0 5.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p -4.0 0.0 2.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 4.0 0.0 2.5
}

shader {
   name marble0
   type marble
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.941176471 0.931372549 0.984313725 }
   diffuse_final { "sRGB nonlinear" 0.031372549 0.019607843 0.000 }
   }

object {
   shader marble0
   type sphere
   name marble
   c 0 0 0
   r 3
}

