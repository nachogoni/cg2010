
image {
   resolution 800 600
   aa 0 0
   samples 1
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 150
   p 0.0 -10.0 10.0
}

camera {
   type pinhole
   eye 0.0 1.0 80.0
   target 0.0 1.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

shader {
  name greenShader
  type constant
  color { "sRGB nonlinear" 0 1 0 }
}

shader {
  name redShader
  type constant
  color { "sRGB nonlinear" 1 0 0 }
}

shader {
  name blueShader
  type constant
  color { "sRGB nonlinear" 0 0 1 }
}

object {
   shader blueShader
   type sphere
   name sphere1
   c 0 0 5
   r 3
}

object {
   shader redShader
   type plane
   p 0 0 0
   n 0 0 1
}
