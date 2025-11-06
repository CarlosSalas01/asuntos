import bcrypt from "bcryptjs";

// Script para generar contraseñas hasheadas
const generatePasswords = async () => {
  const admin123 = await bcrypt.hash("admin123", 10);
  const user123 = await bcrypt.hash("user123", 10);

  console.log("Contraseñas hasheadas:");
  console.log("admin123:", admin123);
  console.log("user123:", user123);
};

generatePasswords();
