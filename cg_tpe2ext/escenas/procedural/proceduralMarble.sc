
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 15.0 40.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 0.0 15.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 0.0 -15.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 0.0 0.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p -14.0 0.0 12.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 60
   p 14.0 0.0 12.5
}

shader {
   name marble0
   type marble
   depth 4
   diffuse_initial { "sRGB nonlinear" 0.741176471 0.731372549 0.784313725 }
   diffuse_final { "sRGB nonlinear" 0.741176471 0.717647059 0.419607843 }
   }

object {
   shader marble0
   type sphere
   name marble
   c 0 0 0
   r 12
}

