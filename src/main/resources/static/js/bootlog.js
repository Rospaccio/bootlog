(function Bootlog()
{
    var codeBlocks = $('#postColumn').find('pre code')
    codeBlocks.each(function (i, block) {
        hljs.highlightBlock(block);
    });
}
)();
