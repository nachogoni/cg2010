
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
   power 60
   p 0.0 0.0 5.0
}

shader {
   name organic0
   type organic
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

shader {
   name wood0
   type wood
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

shader {
   name marble0
   type marble
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.941176471 0.931372549 0.984313725 }
   diffuse_final { "sRGB nonlinear" 0.031372549 0.019607843 0.000 }
}

shader {
   name water0
   type fire
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.000 0.00 1.000 }
   diffuse_final { "sRGB nonlinear" 0.000 0.80 0.40 }
}

shader {
   name fire0
   type fire
   depth 8
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
   type generic-mesh 
   name mesh0
   points 3
      0 3 0
	  -1 1 0
      1 1 0
   triangles 1
   	  0 1 2
}

object {
   shader stone0
   type generic-mesh 
   name mesh1
   points 3
      0 -3 0
	  -1 -1 0
      1 -1 0
   triangles 1
   	  0 1 2
}

object {
   shader water0
   type generic-mesh 
   name mesh0
   points 3
      -2 3 0
	  -4 3 0
      -3 1 0
   triangles 1
   	  0 1 2
}

object {
   shader fire0
   type generic-mesh 
   name mesh0
   points 3
      2 3 0
	  4 3 0
      3 1 0
   triangles 1
   	  0 1 2
}

object {
   shader marble0
   type generic-mesh 
   name mesh0
   points 3
      -2 -3 0
	  -4 -3 0
      -3 -1 0
   triangles 1
   	  0 1 2
}

object {
   shader wood0
   type generic-mesh 
   name mesh0
   points 3
      2 -3 0
	  4 -3 0
      3 -1 0
   triangles 1
   	  0 1 2
}

