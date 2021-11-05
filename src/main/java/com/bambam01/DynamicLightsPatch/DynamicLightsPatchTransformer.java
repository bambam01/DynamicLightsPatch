package com.bambam01.DynamicLightsPatch;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class DynamicLightsPatchTransformer implements IClassTransformer {


    @Override
    public byte[] transform(String name, String newName, byte[] bytes) {
        if (newName.equals("net.minecraft.world.World")) {
            return handleWorldTransform(bytes);
        }

        return bytes;
    }

    public byte[] handleWorldTransform(byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        try {
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
        } catch (Exception e) {
            try {
                cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        for (MethodNode mn : cn.methods) {

            if (mn.name.equals("func_98179_a")) {
                AbstractInsnNode targetNode = null;
                Iterator<AbstractInsnNode> iter = mn.instructions.iterator();
                while (iter.hasNext()) {
                    targetNode = iter.next();
                    if (targetNode.getOpcode() == Opcodes.INVOKESTATIC) {
                        MethodInsnNode node = (MethodInsnNode) targetNode;
                        if (node.owner.equals("atomicstryker/dynamiclights/client/DynamicLights")) {
                            FMLLog.log("DynamicLightsPatch", Level.INFO, "%s", "Patching the patch from DynamicLights with our own path, to patch the DynamicLights patch to prevent DynamicLights in certain dimensions");
                            try {
                                try {
                                    AbstractInsnNode newnode = MethodInsnNode.class.getConstructor(int.class, String.class, String.class, String.class).newInstance(
                                            INVOKESTATIC, "com/bambam01/DynamicLightsPatch/DynamicLightsPatch", "getLightValue", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/Block;III)I");
                                    mn.instructions.set(targetNode, newnode);
                                } catch (NoSuchMethodException e) {
                                    AbstractInsnNode newnode = MethodInsnNode.class.getConstructor(int.class, String.class, String.class, String.class, boolean.class).newInstance(
                                            INVOKESTATIC, "com/bambam01/DynamicLightsPatch/DynamicLightsPatch", "getLightValue", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/Block;III)I", false);
                                    mn.instructions.set(node, newnode);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                FMLLog.log("DynamicLightsPatch", Level.ERROR, "%s", "Dynamic Lights ASM transform failed T_T");
                                return bytes;
                            }
                        }

                    }
                }

            }

        }

        try {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            return cw.toByteArray();
        } catch (Throwable e) {
            ClassWriter cw = new ClassWriter(0);
            cn.accept(cw);
            return cw.toByteArray();
        }
    }

}
