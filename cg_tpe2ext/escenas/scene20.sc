
image {
   resolution 800 600
   aa 0 0
   samples 1
}
/*
light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 3.0
   p 2 2 2
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 2.0
   p -2 2 2
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 1.0
   p 0 -3 2
}
*/

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 10.0
   p 0 -15 20
}

camera {
   type pinhole
   eye 0.0 0.0 10.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

shader {
   name mirror0
   type mirror
   refl { "sRGB nonlinear" 0.200 0.200 0.200 }
}

shader {
   name phong0
   type phong
   diff { "sRGB nonlinear" 1 0 1 }
   spec { "sRGB nonlinear" 1 1 0 } 50
   samples 4
}

shader {
   name phong1
   type phong
   diff { "sRGB nonlinear" 0 1 0 }
   spec { "sRGB nonlinear" 0 0.6 0.8 } 1000
   samples 4
}

shader {
   name phong2
   type phong
   diff { "sRGB nonlinear" 0 0 1 }
   spec { "sRGB nonlinear" 0 1 0 } 200
   samples 4
}

shader {
   name red_sh
   type phong
   diff { "sRGB nonlinear" 1.0 0.0 0.0 }
   spec { "sRGB nonlinear" 1.0 0.0 0.0 } 50
   samples 4
}

shader {
   name glass0
   type glass
   eta 1.333
   color { "sRGB nonlinear" 0 0 0 }
   absorbtion.distance 3
   absorbtion.color { "sRGB nonlinear" 1.0 1.0 1.0 } 50
}

object {
   shader phong0
   type sphere
   name sphere1
   c 0 1.5 0
   r 1.5
}

object {
   shader mirror0
   type sphere
   name sphere2
   c -1.5 -1.5 0
   r 1.5
}

object {
   shader glass0
   type sphere
   name sphere3
   c 1.5 -1.5 0
   r 1.5
}
/*
object {
   shader glass0
   type sphere
   name sphere4
   c 0 0 0
   r 3.5
}
*/
object {
   shader red_sh
   type plane
   p 0 0 -20
   n 0 0 1
}