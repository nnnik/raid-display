package de.nnnik.raiddisplay;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Vec3d;

public enum BlendedBoxRenderer {
	TOP {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
		}
	},
	BOTTOM {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
		}
	},
	LEFT {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
		}
	},
	RIGHT {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
		}
	},
	FRONT {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
		}
	},
	BACK {
		@Override
		public void drawVertexes(BufferBuilder buffer) {
			buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
			buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
			buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
		}
	};
	
	
	private Vec3d offsetCenter;
	private double distSq;
	
	private static double x1,x2,y1,y2,z1,z2;
	private static double size;
	private static float r,g,b,a;
	private static final List<BlendedBoxRenderer> orderedFaces = Arrays.asList(BlendedBoxRenderer.values());
	
	public static void draw(Vec3d blockPos, Vec3d cameraPos, BufferBuilder buffer) {
		Vec3d approachVec = blockPos.subtract(cameraPos);
		for (BlendedBoxRenderer f : BlendedBoxRenderer.values()) {
			f.calculateDistance(approachVec);
		}
		orderedFaces.sort(BlendedBoxFaceTypeComparator.getInstance());
		x1 = blockPos.x+size-cameraPos.x;
		x2 = x1-2*size;
		y1 = blockPos.y+size-cameraPos.y;
		y2 = y1-2*size;
		z1 = blockPos.z+size-cameraPos.z;
		z2 = z1-2*size;
		for (BlendedBoxRenderer f: orderedFaces) {
			f.drawVertexes(buffer);
		}
	}
	
	public static void setColor(float newR, float newG, float newB, float newA) {
		r = newR;
		g = newG;
		b = newB;
		a = newA;
	}
	
	public static void setSize(double newSize) {
		size = newSize;
		TOP.offsetCenter = new Vec3d(0,size,0);
		BOTTOM.offsetCenter = new Vec3d(0,-size,0);
		LEFT.offsetCenter = new Vec3d(-size,0,0);
		RIGHT.offsetCenter = new Vec3d(size,0,0);
		FRONT.offsetCenter = new Vec3d(0,0,size);
		BACK.offsetCenter = new Vec3d(0,0,-size);
	}
	
	private void calculateDistance(Vec3d approachVec) {
		distSq = offsetCenter.add(approachVec).lengthSquared();
	}
	
	public double getDistanceSq() {
		return distSq;
	}
	
	public abstract void drawVertexes(BufferBuilder buffer);
}