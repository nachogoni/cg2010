
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
   power 30
   p 0.0 0.0 15.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 30
   p -14.0 0.0 12.5
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 30
   p 14.0 0.0 12.5
}

shader {
   name wood0
   type wood
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

object {
   shader wood0
   type sphere
   name wood
   c 0 0 0
   r 5
}

