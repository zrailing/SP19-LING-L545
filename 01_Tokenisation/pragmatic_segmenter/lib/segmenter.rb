require 'pragmatic_segmenter'

lang = "en"
if ARGV[0]
    lang=ARGV[0]
end

STDIN.each_with_index do |line, idx|
    ps = PragmaticSegmenter::Segmenter.new(text: line, language: lang)
    ps.segment
    for i in ps.segment
        print(i,"\n")
    end
end