image {
  resolution 640 480
  aa 0 2
  samples 4
  filter mitchell
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 20
   p 20.0 0.0 0.0
}

camera {
  type pinhole
  eye    0 -5 0
  target 0 1 0
  up     0 0 1
  fov    60
  aspect 12
}

shader {
  name blue
  type constant
  color { "sRGB nonlinear" 0.25 0.25 1.0 }
}

shader {
  name red
  type constant
  color { "sRGB nonlinear" 1.0 0.25 0.25 }
}

object {
  type box
  shader red
  transform {
	rotatex 30.0
	rotatey 30.0
	translate 2.0 0 0
  }
}

object {
  type box
  shader blue
  transform {
	translate -2.0 0 0
	scaleu 2.0
  }
}


