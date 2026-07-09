#!/usr/bin/env bash
# SecureGuard installer (Linux/macOS)
# Copies the jar into ~/.secureguard and adds a `secureguard` command to your PATH.
set -e
INSTALL_DIR="$HOME/.secureguard"
JAR_SRC="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/target/SecureGuard.jar"

mkdir -p "$INSTALL_DIR"
cp "$JAR_SRC" "$INSTALL_DIR/SecureGuard.jar"

cat > "$INSTALL_DIR/secureguard" << 'WRAPPER'
#!/usr/bin/env bash
exec java -jar "$HOME/.secureguard/SecureGuard.jar" "$@"
WRAPPER
chmod +x "$INSTALL_DIR/secureguard"

BIN_DIR="$HOME/.local/bin"
mkdir -p "$BIN_DIR"
ln -sf "$INSTALL_DIR/secureguard" "$BIN_DIR/secureguard"

if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
  echo "Add this to your ~/.bashrc or ~/.zshrc, then restart your terminal:"
  echo "  export PATH=\"\$HOME/.local/bin:\$PATH\""
fi

echo "Installed. Try: secureguard ."
