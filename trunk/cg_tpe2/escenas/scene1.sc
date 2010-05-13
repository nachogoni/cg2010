
image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 0.0 0.0 20.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}


light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 2
   p 20.0 0.0 0.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 2
   p -20.0 0.0 0.0
}
/*
light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 1
   p 0.0 0.0 20.0
}
*/
light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 4
   p 10.0 10.0 10.0
}

shader {
   name phong1
   type phong
   diff { "sRGB nonlinear" 0 0 1 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong2
   type phong
   diff { "sRGB nonlinear" 1 0 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong3
   type phong
   diff { "sRGB nonlinear" 0 1 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong7
   type phong
   diff { "sRGB nonlinear" 1 1 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name mirror2
   type mirror
   refl { "sRGB nonlinear" 1.0 0.0 0.0 }
}

shader {
   name mirror1
   type mirror
   refl { "sRGB nonlinear" 0.0 0.0 1.0 }
}

object { 
   shader mirror1
   type generic-mesh 
   name mesh1
   points 3
      0 5 0
      0 0 1
      -7 0 0
   triangles 1
   	  0 1 2
}

object { 
   shader mirror1
   type generic-mesh 
   name mesh2
   points 3
      0 5 0
	  7 0 0
      0 0 1
   triangles 1
   	  0 1 2
}

object {
   shader mirror2
   type sphere
   name sphere1
   c 0 6 0
   r 1
}

object {
   shader phong2
   type sphere
   name sphere2
   c 7 0 0
   r 0.5
}

object {
   shader phong2
   type sphere
   name sphere3
   c -7 0 0
   r 0.5
}

object {
   shader mirror2
   type sphere
   name sphere4
   c 0 0 7
   r 0.5
}

object {
   shader phong2
   type sphere
   name sphere5
   c 0 0 -7
   r 0.5
}

object {
   shader phong2
   type sphere
   name sphere6
   c -7 4 0
   r 0.5
}

object {
	shader phong7
	type plane
	name plane1
	p 0 0 0
	n 0 0 1 
}
