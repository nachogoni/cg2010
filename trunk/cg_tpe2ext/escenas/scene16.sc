
image {
   resolution 800 600
   aa 0 2
   samples 4
}


light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 4.0
   p 10.0 0 5.0
}

camera {
   type pinhole
   eye 3.0 0.0 3.0
   target 0.0 0.0 0.0
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
   name phong0
   type phong
   diff { "sRGB nonlinear" 0.5 0.5 0.5 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object {
   shader phong1
   type sphere
   name sphere2
   c -1 0 1
   r 0.5
}
/*
object {
   shader phong0
   type sphere
   name sphere1
   c 3 0 0
   r 3
}
*/

object {
   shader phong0
   type plane
   p 0 0 0
   n 0 0 1
}
