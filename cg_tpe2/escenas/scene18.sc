
image {
   resolution 800 600
   aa 0 2
   samples 4
}


light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 30.0
   p 0 5.0 5.0
}

camera {
   type pinhole
   eye 0.0 1.0 10.0
   target 0.0 1.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

shader {
   name phong1
   type phong
   diff { "sRGB nonlinear" 1 0 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name mirror0
   type mirror
   refl { "sRGB nonlinear" 0.800 0.800 0.800 }
}

shader {
   name mirror1
   type mirror
   refl { "sRGB nonlinear" 0.400 0.400 0.400 }
}

shader {
   name phong0
   type phong
   diff { "sRGB nonlinear" 1.0 1.0 1.0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name blue_sh
   type phong
   diff { "sRGB nonlinear" 0.0 0.0 1.0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name yellow_sh
   type phong
   diff { "sRGB nonlinear" 1.0 1.0 0.0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name glass0
   type glass
   eta 0.5
   color { "sRGB nonlinear" 0 0 0 }
   absorbtion.distance 3
   absorbtion.color { "sRGB nonlinear" 1.0 1.0 1.0 } 50
}

object {
   shader phong1
   type sphere
   name sphere2
   c 0 0 0
   r 1.5
}

object {
   shader glass0
   type sphere
   name sphere3
   c 0 1 4
   r 0.5
}

object {
   shader yellow_sh
   type plane
   p 2 0 0
   n -1 0 0
}


object {
   shader blue_sh
   type plane
   p -2 0 0
   n 1 0 0
}

object {
   shader mirror0
   type plane
   p 0 0 0
   n 0 1 0
}

