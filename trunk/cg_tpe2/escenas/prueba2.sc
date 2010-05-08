shader {
   name Mirror
   type mirror
   refl { "sRGB nonlinear" 0.800 0.800 0.800 }
}

object { 
   shader Mirror
   type generic-mesh 
   name meshName
   points 4 
      0 0 0
      2 0 0 
      0 2 0
      0 0 2
   triangles 2 
      1 2 3
      2 3 4
   normals vertex
	  1 1 1
	  2 2 2
	  3 3 3
	  4 4 4
	uvs facevarying
	  1 2 3 4 5 6 7 8   
}

object { 
   shader Mirror
   type generic-mesh 
   name meshName
   points 4 
      0 0 0
      2 0 0 
      0 2 0
      0 0 2
   triangles 2 
      1 2 3
      2 3 4
   normals vertex
	  1 1 1
	  2 2 2
	  3 3 3
	  4 4 4  
}

object { 
   shader Mirror
   type generic-mesh 
   name meshName
   points 4 
      0 0 0
      2 0 0 
      0 2 0
      0 0 2
   triangles 2 
      1 2 3
      2 3 4
	uvs facevarying
	  1 2 3 4 5 6 7 8   
}

object { 
   shader Mirror
   type generic-mesh 
   name meshName
   points 4 
      0 0 0
      2 0 0 
      0 2 0
      0 0 2
   triangles 2 
      1 2 3
      2 3 4
}