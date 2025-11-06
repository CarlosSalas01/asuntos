export const errorHandler = (err, req, res, next) => {
  console.error(err.stack);

  const error = {
    message: err.message || "Error interno del servidor",
    status: err.status || 500,
  };

  if (process.env.NODE_ENV === "development") {
    error.stack = err.stack;
  }

  res.status(error.status).json({
    error: error.message,
    ...(error.stack && { stack: error.stack }),
  });
};

export const notFound = (req, res, next) => {
  const error = new Error(`Recurso no encontrado - ${req.originalUrl}`);
  error.status = 404;
  next(error);
};
