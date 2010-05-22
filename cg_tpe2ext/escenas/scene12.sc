image {
  resolution 640 480
  aa 0 2
  filter mitchell
}

camera {
  type pinhole
  eye    0 0 0
  target 0 1 0
  up     0 0 1
  fov    60
  aspect 1.333333
}

shader {
  name default
  type constant
  color { "sRGB nonlinear" 1 0 0 }
}

object {
  shader default
  type sphere
  c 0 10 0
  r 1
}

object {
  shader default
  type sphere
  c 2 10 0
  r 1
}


object {
  shader default
  type sphere
  c 4 10 0
  r 1
}


object {
  shader default
  type sphere
  c 6 10 0
  r 1
}

object {
  shader default
  type sphere
  c 8 10 0
  r 1
}


