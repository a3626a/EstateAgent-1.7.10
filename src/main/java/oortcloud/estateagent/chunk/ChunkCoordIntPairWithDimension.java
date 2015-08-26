package oortcloud.estateagent.chunk;

import net.minecraft.world.ChunkCoordIntPair;

public class ChunkCoordIntPairWithDimension extends ChunkCoordIntPair {

	public final int dim;
	
	public ChunkCoordIntPairWithDimension(int dim, int x, int y) {
		super(x, y);
		this.dim = dim;
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (this == obj)
	        {
	            return true;
	        }
	        else if (!(obj instanceof ChunkCoordIntPairWithDimension))
	        {
	            return false;
	        }
	        else
	        {
	        	ChunkCoordIntPairWithDimension chunkcoordintpair = (ChunkCoordIntPairWithDimension)obj;
	            return this.dim == chunkcoordintpair.dim && this.chunkXPos == chunkcoordintpair.chunkXPos && this.chunkZPos == chunkcoordintpair.chunkZPos;
	        }
	}
	
	@Override
	public String toString() {
		return "[" + this.dim + ", " + this.chunkXPos + ", " + this.chunkZPos + "]";
	}

}
