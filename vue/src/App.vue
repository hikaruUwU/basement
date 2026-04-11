<template>


  <router-view/>
</template>

<script setup lang="tsx">

import {$dialog} from "./service/dialog/dialoging.tsx";

const { close } = $dialog().call(
    $dialog().preset.plain({
      content: "Content",
      onBoth: () => close()
    })
)

</script>