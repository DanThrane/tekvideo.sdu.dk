set -e

bower install

outDir="build"
rm -rf $outDir
mkdir -p $outDir

entryPoints=(
    "components/tv-browser.html"
    "components/exercise-editor/tve-group-renderer.html"
    "components/exercise-editor/tve-group-editor.html"
    "components/tekvideo-exercise-card.html"
)

function build {
    printf "Processing file: '%s'\n" "$1"
    dir=$(dirname "$outDir/$1")
    mkdir -p $dir
    vulcanize $1 --strip-comments --inline-scripts --inline-css > $outDir/$1
}

for item in "${entryPoints[@]}"
do
    build $item
done

