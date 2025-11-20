import { useState } from "react";
import {
  InegiLogo,
  SpinnerIcon,
  ErrorIcon,
  UserIcon,
  LockIcon,
} from "../../components/icons/CustomIcons";
import { apiService } from "../../services/apiService";

const Login = ({ onLogin }) => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Limpiar error cuando el usuario empiece a escribir
    if (error) setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    // Validación básica
    if (!formData.username || !formData.password) {
      setError("Por favor, completa todos los campos");
      setIsLoading(false);
      return;
    }

    try {
      // Llamada real a la API
      const response = await apiService.login({
        username: formData.username,
        password: formData.password,
      });

      // Guardar información del usuario en localStorage
      localStorage.setItem("authToken", response.token);
      localStorage.setItem("userRole", response.user.role);
      localStorage.setItem("userData", JSON.stringify(response.user));

      // Notificar al componente padre sobre el login exitoso
      onLogin(response.user.username, response.user.role, response.user);
    } catch (error) {
      console.error("Error en login:", error);
      setError(
        error.message ||
          "Error de conexión. Verifica que el servidor esté ejecutándose."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-950 to-teal-950 py-12 px-4 sm:px-6 lg:px-8">
      <div className="flex flex-col items-center justify-center w-full max-w-md space-y-8">
        {/* Logo centrado por encima del formulario */}
        <div className="flex justify-center">
          <InegiLogo className="h-40 w-40 filter brightness-0 invert  drop-shadow-lg" />
        </div>

        {/* Contenedor del formulario */}
        <div className="bg-gray-900 p-8 rounded-lg shadow-lg w-full">
          <div className="w-full space-y-8">
            <div className="text-center">
              <h2 className="text-3xl font-extrabold text-gray-100">
                Sistema de Asuntos
              </h2>
              <p className="mt-2 text-center text-sm text-gray-600">
                Accede a tu cuenta para continuar
              </p>
            </div>

            <form className="mt-8 space-y-6 w-full" onSubmit={handleSubmit}>
              <div className="space-y-4">
                <div className="relative">
                  <label htmlFor="username" className="sr-only">
                    Usuario
                  </label>
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <UserIcon className="h-5 w-5 text-gray-400" />
                  </div>
                  <input
                    id="username"
                    name="username"
                    type="text"
                    required
                    className="text-center appearance-none relative block w-full py-3 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-900 focus:border-blue-900 focus:z-10 sm:text-sm"
                    placeholder="Usuario"
                    value={formData.username}
                    onChange={handleChange}
                    disabled={isLoading}
                  />
                </div>
                <div className="relative">
                  <label htmlFor="password" className="sr-only">
                    Contraseña
                  </label>
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <LockIcon className="h-5 w-5 text-gray-400" />
                  </div>
                  <input
                    id="password"
                    name="password"
                    type="password"
                    required
                    className="text-center appearance-none relative block w-full py-3 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-900 focus:border-blue-900 focus:z-10 sm:text-sm"
                    placeholder="Contraseña"
                    value={formData.password}
                    onChange={handleChange}
                    disabled={isLoading}
                  />
                </div>
              </div>

              {error && (
                <div className="rounded-md bg-red-900 p-2">
                  <div className="flex items-center justify-center">
                    <ErrorIcon className="h-5 w-5 text-red-400 mx-2" />
                    <h3 className="text-sm font-medium text-red-400 text-center">
                      {error}
                    </h3>
                  </div>
                </div>
              )}

              <div>
                <button
                  type="submit"
                  disabled={isLoading}
                  className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-lg text-white bg-blue-900 hover:bg-blue-950 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
                >
                  {isLoading ? (
                    <div className="flex items-center">
                      <SpinnerIcon className="-ml-1 mr-3 h-5 w-5 text-white" />
                      Accediendo...
                    </div>
                  ) : (
                    "Acceder"
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
