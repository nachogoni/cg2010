
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

shader {
   name phong1
   type phong
   texture "earth.jpg"
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong0
   type phong
   texture "earth.jpg"
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object {
   shader phong0
   type sphere
   name sphere2
   c -4 0 0
   r 3
}

object {
   shader phong0
   type sphere
   name sphere1
   c 4 0 0
   r 3
}

object { 
   shader phong1 
   type generic-mesh 
   name mesh1
   points 3
      +0 +0 +5
      +0 +5 +5
      +5 +5 +5
   triangles 1 
      0 1 2
	uvs vertex
	  0.0 1.0
	  0.0 0.0
	  1.0 0.0
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 10.0
   p 0.0 20.0 10.0
}