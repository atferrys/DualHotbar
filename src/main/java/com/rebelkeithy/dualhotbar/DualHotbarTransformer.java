package com.rebelkeithy.dualhotbar;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class DualHotbarTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String className, String newClassName, byte[] data) {
        
        boolean isObfuscated = !(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        if(newClassName.equals("net.minecraft.entity.player.InventoryPlayer")) {
            System.out.println("********* INSIDE InventoryPlayer TRANSFORMER ABOUT TO PATCH: " + className);

            if(!isObfuscated) {
                data = patchBipush(className, "isHotbar", "(I)Z", data);
                data = patchBipush(className, "getHotbarSize", "()I", data);
                data = patchBipush(className, "changeCurrentItem", "(I)V", data);
                return patchBipush(className, "getBestHotbarSlot", "()I", data);
            } else {
                data = patchBipush(className, "func_184435_e", "(I)Z", data);
                data = patchBipush(className, "func_70451_h", "()I", data);
                data = patchBipush(className, "func_70453_c", "(I)V", data);
                return patchBipush(className, "func_184433_k", "()I", data);
            }
        }

        if(className.equals("net.minecraftforge.common.ForgeHooks")) {
            System.out.println("********* INSIDE ForgeHooks TRANSFORMER ABOUT TO PATCH: " + className);
            return patchBipush2(isObfuscated, className, "onPickBlock", null, data);
        }

        if(className.equals("net.minecraftforge.client.GuiIngameForge")) {
            System.out.println("********* INSIDE GuiIngameForge TRANSFORMER ABOUT TO PATCH: " + className);
            return patchShift(className, "renderToolHighlight", null, data);
        }

        return data;
    }

    private byte[] patchBipush(String className, String methodName, String methodDesc, byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(methodName) && (methodDesc == null || methodNode.desc.equals(methodDesc))) {
                System.out.println("In " + methodName);

                Iterator<AbstractInsnNode> insnIter = methodNode.instructions.iterator();
                AbstractInsnNode insnNode = insnIter.next();

                while (insnIter.hasNext()) {
                    if(insnNode.getOpcode() == Opcodes.BIPUSH) {
                        System.out.println("found instruction to replace");

                        AbstractInsnNode newInstruction = new FieldInsnNode(Opcodes.GETSTATIC, "com/rebelkeithy/dualhotbar/DualHotbarMod", "hotbarSize", "I");

                        methodNode.instructions.insert(insnNode, newInstruction);
                        methodNode.instructions.remove(insnNode);
                    }
                    insnNode = insnIter.next();
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchBipush2(boolean isObfuscated, String className, String methodName, String methodDesc, byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(methodName) && (methodDesc == null || methodNode.desc.equals(methodDesc))) {
                System.out.println("In " + methodName);

                Iterator<AbstractInsnNode> insnIter = methodNode.instructions.iterator();

                System.out.println("Number of instructions " + methodNode.instructions.size());

                while (insnIter.hasNext()) {
                    AbstractInsnNode insnNode = insnIter.next();

                    if(insnNode instanceof IntInsnNode) {
                        System.out.println(((IntInsnNode) insnNode).operand);
                    }

                    if(insnNode.getOpcode() == Opcodes.BIPUSH) {
                        System.out.println("found instruction to replace");

                        InsnList insnList = new InsnList();

                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", isObfuscated ? "field_71071_by" : "inventory", "Lnet/minecraft/entity/player/InventoryPlayer;"));
                        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", isObfuscated ? "field_70461_c" : "currentItem", "I"));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/rebelkeithy/dualhotbar/DualHotbarMod", "inventorySlotOffset", "(I)I", false));

                        methodNode.instructions.insert(insnNode, insnList);
                        methodNode.instructions.remove(insnNode);
                        break;
                    }
                }
            }
        }


        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchShift(String className, String methodName, String methodDesc, byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(methodName) && (methodNode.desc.equals(methodDesc) || methodDesc == null)) {
                System.out.println("In " + methodName);

                Iterator<AbstractInsnNode> insnIter = methodNode.instructions.iterator();


                AbstractInsnNode insnNode = insnIter.next();

                AbstractInsnNode newInstruction = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/rebelkeithy/dualhotbar/RenderHandler", "shiftUp", "()V", false);
                methodNode.instructions.insertBefore(insnNode, newInstruction);

                while(insnNode.getOpcode() != Opcodes.RETURN) {
                    insnNode = insnIter.next();
                }

                newInstruction = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/rebelkeithy/dualhotbar/RenderHandler", "shiftDown", "()V", false);
                methodNode.instructions.insertBefore(insnNode, newInstruction);
            }
        }


        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

}
