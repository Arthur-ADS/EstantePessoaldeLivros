# Estante Pessoal de Livros

**Autor:** Arthur

## Decisões técnicas

- **Conversor de tipos do enum:** StatusLeitura é convertido para String via
  @TypeConverter (Converters.kt), usando `.name`/`valueOf()`, e registrado no
  banco com @TypeConverters(Converters::class) na AppDatabase.

- **Local escolhido para a ordenação:** resolvida na camada de repositório
  (EstanteRepository), combinando o Flow de livros do Room com o Flow de
  ordenação do DataStore via `combine()`, e ordenando em memória com
  sortedBy/sortedByDescending. Optou-se por não fazer isso em SQL porque a
  ordenação depende de uma preferência que vive fora do banco.
