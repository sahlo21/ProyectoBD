public interface IProductoDAO {
    void crear(Producto producto);
    Producto buscarPorId(int id);
    List<Producto> listarTodos();
    void actualizar(Producto producto);
    void eliminar(int id);
}
