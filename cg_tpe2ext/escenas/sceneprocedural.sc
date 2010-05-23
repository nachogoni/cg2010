
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
   power 40
   p 0.0 0.0 5.0
}

shader {
   name organic0
   type organic
   depth 3
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
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
   diffuse_initial { "sRGB nonlinear" 0.031372549 0.019607843 0.000 }
   diffuse_final { "sRGB nonlinear" 0.741176471 0.631372549 0.584313725 }
}

shader {
   name water0
   type water
   depth 6
   diffuse_initial { "sRGB nonlinear" 0.200 0.300 1.000 }
   diffuse_final { "sRGB nonlinear" 0.100 0.150 0.500 }
}

shader {
   name fire0
   type fire
   depth 7
   diffuse_initial { "sRGB nonlinear" 1.000 0.000 0.000 }
   diffuse_final { "sRGB nonlinear" 1.000 1.000 0.000 }
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

