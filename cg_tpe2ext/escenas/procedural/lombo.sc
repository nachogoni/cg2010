%% common settings

image {
   resolution 600 600
   aa 0 0
}

accel bih
filter mitchell
bucket 32 row

shader {
   name organic0
   type organic
   depth 8
   diffuse_initial { "sRGB nonlinear" 0.30 0.30 0.0 }
   diffuse_final { "sRGB nonlinear" 01.0 01.0 01.0 }
}

shader {
   name wood0
   type wood
   depth 5
   diffuse_initial { "sRGB nonlinear" 0.392156863 0.196078431 0.000 }
   diffuse_final { "sRGB nonlinear" 0.784313726 0.392156863 0.000 }
}

shader {
   name marble0
   type marble
   depth 5
   diffuse_initial { "sRGB nonlinear" 0.466667 0.34000 0.066666 }
   diffuse_final { "sRGB nonlinear" 0.91372549 0.705882353 0.633333333 }
}

shader {
   name water0
   type water
   depth 4
   diffuse_initial { "sRGB nonlinear" 0.000 0.00 0.5000 }
   diffuse_final { "sRGB nonlinear" 0.00 0.50 0.50 }
}

shader {
   name fire0
   type fire
   depth 5
   diffuse_initial { "sRGB nonlinear" 1.000 1.000 0.000 }
   diffuse_final { "sRGB nonlinear" 1.000 0.15000 0.000 }
}

shader {
   name stone0
   type stone
   depth 4
   diffuse_initial { "sRGB nonlinear" 0.2000 0.2000 0.2000 }
   diffuse_final { "sRGB nonlinear" 01.000 01.000 01.000 }
}


%% camera

camera {
   type pinhole
   eye    0 -15 8
   target 0 0 0
   up     0 0 1
   fov    40
   aspect 1
}


%% light sources

light {
  type point
  color 1 1 1
  power 100
  p
     -4.25819396973 -4.8784570694 5.70054674149
}

light {
  type point
  color 1 1 1
  power 100
  p
     -5.13696432114 -5.61583280563 4.06224298477
}

light {
  type point
  color 1 1 1
  power 100
  p
     -6.422539711 -4.08374404907 4.06224298477
}

light {
  type point
  color 1 1 1
  power 100
  p
     -5.54376888275 -3.34636831284 5.70054721832
}

%% geometry

object {
   shader water0
   type generic-mesh
   name "Plane"
   points 8
       3.1  3.1 0
       3.1 -3.1 0
      -3.1 -3.1 0
      -3.1  3.1 0
      -3.1  3.1 -0.61
      -3.1 -3.1 -0.61
       3.1 -3.1 -0.61
       3.1  3.1 -0.61
       
   triangles 12
      0 3 2
      0 2 1
      2 3 4
      2 4 5
      3 0 7
      3 7 4
      0 1 6
      0 6 7
      1 2 5
      1 5 6
      5 4 7
      5 7 6
   normals none
   uvs facevarying
		0.500000 0.500000 0.300000 0.400000 1.000000 1.00000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
		0.100000 0.200000 0.300000 0.400000 0.500000 0.600000
}
}
