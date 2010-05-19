image {
  resolution 320 240
  aa 0 1
  filter mitchell
}

camera {
  type pinhole
  eye    -10.5945 -30.0581 10.967
  target 0.0554193 0.00521195 5.38209
  up     0 0 1
  fov    60
  aspect 1.333333
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 20.0
   p 0.5 4 5.0
}

shader {
  name blueShader
  type constant
  color { "sRGB nonlinear" 0 0 1 }
}



shader {
  name whiteShader
  type constant
  color { "sRGB nonlinear" 1 1 1 }
}

shader {
   name greenShader
   type glass
   eta 1.0
   color { "sRGB nonlinear" 0.800 0.800 0.800 }
   absorbtion.distance 5.0
   absorbtion.color { "sRGB nonlinear" 1.0 1.0 1.0 }
}


shader {
   name redShader
   type mirror
   refl { "sRGB nonlinear" 0.800 0.800 0.800 }
}

object {
  shader greenShader
  type plane
  p 0 0 0
  n 0 0 1
}

object {
  shader redShader
  type sphere
  c 12 0 5
  r 3
}


object {
  shader whiteShader
  type sphere
  c -13 0 5
  r 3
}
