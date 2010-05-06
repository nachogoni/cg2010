image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 7.0 -6.0 5.0
   target 6.0 -5.0 4.0
   up -0.30 0.30 0.90
   fov 49.134 
   aspect 1.333
}

light {
   type point
   color { "sRGB nonlinear" 1.000 1.000 1.000 }
   power 100.0
   p 1.0 3.0 6.0
}

shader {
   name Mirror
   type mirror
   refl { "sRGB nonlinear" 0.800 0.800 0.800 }
}

object {
   shader Mirror
   type sphere
   name mirror
   c 5 0 0
   r 3
   transform { rotatex 45 translate 1 2 3 }
}

object {
   shader Mirror
   type sphere
   name mirror1
   c -5 0 0
   r 3
}

object {
   shader Mirror
   type plane
   p -7 0 0
   n 1 0 1
}

object {
   shader Mirror
   type plane
   p 7 0 0
   n -1 0 -1
}

object {
   shader Mirror
   type plane
   p 0 -5 0
   n 0 1 1
}

object { 
   shader default 
   type generic-mesh 
   name meshName
   points 3 
      0 0 0
      2 0 0 
      0 2 0
      -2 0 0     
   triangles 2 
      1 2 3
      2 3 4
   normals vertex
	  1 1 1
	  2 2 2
	  3 3 3
	uvs facevarying
	  1 2 3 4 5 6   
}