sed 's/[^а-яА-ЯIӀ]\+/\n/g' | grep -v '^$' > wiki.words
tail -n +2 wiki.words > wiki.nextwords
paste wiki.words wiki.nextwords | sort | uniq -c > wiki.bigrams