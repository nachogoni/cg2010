
image {
   resolution 800 600
   aa 0 1
   samples 1
}

camera {
   type pinhole
   eye 0.0 0.0 10.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 11
   p 2.0 0.0 5.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 11
   p -2.0 0.0 5.0
}


shader {
   name organic0
   type organic
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.80 0.60 1.0 }
   diffuse_final { "sRGB nonlinear" 0.40 0.30 0.00 }
}

shader {
   name wood0
   type wood
   depth 4
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

shader {
   name marble0
   type marble
   depth 5
   diffuse_initial { "sRGB nonlinear" 0.841176471 0.0 0.0 }
   diffuse_final { "sRGB nonlinear" 0.9 0.9 0.9 }
}

shader {
   name water0
   type water
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.000 0.00 1.000 }
   diffuse_final { "sRGB nonlinear" 0.000 0.80 0.40 }
}

shader {
   name fire0
   type fire
   depth 8
   diffuse_initial { "sRGB nonlinear" 1.000 1.000 0.000 }
   diffuse_final { "sRGB nonlinear" 1.000 0.2000 0.000 }
}

shader {
   name stone0
   type stone
   diffuse_initial { "sRGB nonlinear" 1.000 1.000 1.000 }
   diffuse_final { "sRGB nonlinear" 0.000 0.000 0.000 }
}

object {
   shader organic0
   type sphere
   name sphere0
   c 0 1.5 0
   r 1.5
}

object {
   shader stone0
   type sphere
   name sphere1
   c 0 -1.5 0
   r 1.5
}

object {
   shader water0
   type sphere
   name sphere2
   c -3 1.5 0
   r 1.5
}

object {
   shader fire0
   type sphere
   name sphere3
   c 3 1.5 0
   r 1.5
}

object {
   shader marble0
   type sphere
   name sphere4
   c -3 -1.5 0
   r 1.5
}

object {
   shader wood0
   type sphere
   name sphere5
   c 3 -1.5 0
   r 1.5
}

