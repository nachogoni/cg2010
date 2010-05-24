
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
   name water0
   type water
   depth 4
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

object {
   shader water0
   type plane
   name water
   p 0 0 0
   n 0 0 1
}

